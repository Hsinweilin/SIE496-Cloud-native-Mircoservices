import http from "k6/http";
import { check, sleep } from "k6";


// HOW TO RUN:
// docker pull grafana/k6
// cd stress-testing

// docker run --rm -i -v ${PWD}:/scripts grafana/k6 run /scripts/stress-test.js

// OR, to print results to a file:
// docker run --rm -i -v ${PWD}:/scripts grafana/k6 run --out json=/scripts/results.json /scripts/stress-test.js


// defining the test scenarios for different services
export let options = {
	scenarios: {
		inventory: {
			executor: "constant-vus", // keeps the same number of users throughout
			vus: 100, // 100 users
			// 2 mins
			duration: "2m",
			exec: "inventoryTest",
		},
		payment: {
			executor: "ramping-vus", // gradually increases and decreases users
			startVUs: 0, // start with no users
			stages: [
				{ duration: "1m", target: 200 }, // ramp up to 200 users in 1 min
				{ duration: "2m", target: 200 }, // hold at 200 users for 2 mins
				{ duration: "1m", target: 0 }, // ramp down to 0 users in 1 min
			],
			exec: "paymentTest",
		},
		review: {
			executor: "per-vu-iterations", // each user does a set number of iterations
			vus: 50, // 50 users
			iterations: 20, // 20 requests per user
			exec: "reviewTest",
		},
		gateway: {
			executor: "shared-iterations", // total iterations shared among users
			vus: 50, // 50 users
			iterations: 500, // 500 total requests
			exec: "gatewayTest",
		},
	},
};

// test for inventory service
export function inventoryTest() {
	// send a GET request
	let res = http.get("http://localhost:8081/v1/inventory-service"); // make sure this endpoint is correct
	check(res, { "status is 200": (r) => r.status === 200 });
	// wait for 1 second before the next request
	sleep(1);
}

// test for payment service
export function paymentTest() {
	let res = http.post(
		"http://localhost:8082/v1/payment-service",
		JSON.stringify({ amount: 100 }),
		{
			headers: { "Content-Type": "application/json" }, // setting the content type
		}
	);
	// make sure response status is 200
	check(res, { "status is 200": (r) => r.status === 200 });
	sleep(1);
}

// test for review service
export function reviewTest() {
	// GET request to fetch reviews
	let res = http.get("http://localhost:8083/v1/review-service"); // replace with actual endpoint if different
	// check if the response is 200
	check(res, { "status is 200": (r) => r.status === 200 });
	// 1 second wait
	sleep(1);
}

// test for gateway service
export function gatewayTest() {
	// hitting the gateway endpoint
	let res = http.get("http://localhost:8072/v1/gateway-service"); // gateway endpoint, double-check this
	// check for 200
	check(res, { "status is 200": (r) => r.status === 200 });
	// 1 second wait
	sleep(1);
}
