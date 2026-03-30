sap.ui.define(['sap/ui/core/mvc/ControllerExtension'], function (ControllerExtension) {
	'use strict';

	return ControllerExtension.extend('books.ext.controller.ListReportExtension', {
		// this section allows to extend lifecycle hooks or hooks provided by Fiori elements
		override: {
			/**
			 * Called when a controller is instantiated and its View controls (if available) are already created.
			 * Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
			 * @memberOf books.ext.controller.ListReportExtension
			 */
			onInit: async function () {

				setTimeout(() => {
					this._callActions();
				}, 0);
				// you can access the Fiori elements extensionAPI via this.base.getExtensionAPI
				// 	const oModel = this.base.getView().getModel();
				// 		console.log("oModel =", oModel);
				// 	const sServiceUrl = oModel.sServiceUrl || oModel.getServiceUrl?.();
				// 	console.log("serviceUrl =", sServiceUrl);

				// 	const tokenRes = await fetch(sServiceUrl, {
				// 		method: "GET",
				// 		headers: {
				// 			"X-CSRF-Token": "Fetch"
				// 		}
				// 	});
				// 	const csrfToken = tokenRes.headers.get("X-CSRF-Token");
				// 	try {
				// 		const response = await fetch(`${sServiceUrl}writeLog`, {
				// 			method: "POST",
				// 			headers: {
				// 				"Content-Type": "application/json",
				// 				"X-CSRF-Token": csrfToken
				// 			},
				// 			body: JSON.stringify({
				// 				objectType: "infomation",
				// 				objectId: "000",
				// 				operation: "init",
				// 				detail: "画面初期化"
				// 			})
				// 		});

				// 		const userInfo = await fetch(`${sServiceUrl}getUserInfo`, {
				// 			method: "POST",
				// 			headers: {
				// 				"Content-Type": "application/json",
				// 				"X-CSRF-Token": csrfToken
				// 			}
				// 		});

				// 		console.log("userInfo:", await userInfo.json());

				// 		if (!response.ok) {
				// 			throw new Error("HTTP error: " + response.status);
				// 		}

				// 		const result = await response.json();
				// 		console.log("success:", result);
				// 	} catch (e) {
				// 		console.error("request failed:", e);
				// 	}
				// }
			}
		},
		_callActions: async function () {
			const oView = this.base.getView();
			const oModel = oView.getModel();

			console.log("oModel =", oModel);

			if (!oModel) {
				console.error("OData model not found");
				return;
			}

			const sServiceUrl = oModel.sServiceUrl || (oModel.getServiceUrl && oModel.getServiceUrl());
			console.log("serviceUrl =", sServiceUrl);

			if (!sServiceUrl) {
				console.error("Service URL not found");
				return;
			}

			try {
				const tokenRes = await fetch(sServiceUrl, {
					method: "GET",
					headers: {
						"X-CSRF-Token": "Fetch"
					}
				});
				const csrfToken = tokenRes.headers.get("X-CSRF-Token");

				const response = await fetch(`${sServiceUrl}writeLog`, {
					method: "POST",
					headers: {
						"Content-Type": "application/json",
						"X-CSRF-Token": csrfToken
					},
					body: JSON.stringify({
						objectType: "infomation",
						objectId: "000",
						operation: "init",
						detail: "画面初期化"
					})
				});

				if (!response.ok) {
					const errText = await response.text();
					throw new Error("writeLog failed: " + response.status + " " + errText);
				}

				const userInfo = await fetch(`${sServiceUrl}getUserInfo`, {
					method: "POST",
					headers: {
						"Content-Type": "application/json",
						"X-CSRF-Token": csrfToken
					}
				});

				if (!userInfo.ok) {
					const errText = await userInfo.text();
					throw new Error("getUserInfo failed: " + userInfo.status + " " + errText);
				}

				console.log("userInfo:", await userInfo.json());
				console.log("success:", await response.json());

			} catch (e) {
				console.error("request failed:", e);
			}
		}
	
	});
});
