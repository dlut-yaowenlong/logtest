sap.ui.define(['sap/ui/core/mvc/ControllerExtension'], function (ControllerExtension) {
	'use strict';

	return ControllerExtension.extend('books.ext.controller.ListReportExtension', {
		override: {
			onInit: function () {
				// 画面初期化後に処理を実行する
				setTimeout(() => {
					this._callActions();
				}, 0);
			}
		},

		// CAP の Action / Function を呼び出す共通処理
		_callActions: async function () {
			const oView = this.base.getView();
			const oModel = oView.getModel();

			// 取得した ODataModel を確認
			console.log("oModel =", oModel);

			// ODataModel が取得できない場合は処理終了
			if (!oModel) {
				console.error("OData model not found");
				return;
			}

			// サービスURLを取得
			const sServiceUrl = oModel.sServiceUrl || (oModel.getServiceUrl && oModel.getServiceUrl());
			console.log("serviceUrl =", sServiceUrl);

			// サービスURLが取得できない場合は処理終了
			if (!sServiceUrl) {
				console.error("Service URL not found");
				return;
			}

			try {
				// CSRFトークンを取得
				const tokenRes = await fetch(sServiceUrl, {
					method: "GET",
					headers: {
						"X-CSRF-Token": "Fetch"
					}
				});
				const csrfToken = tokenRes.headers.get("X-CSRF-Token");

				// writeLog Action を呼び出してログを書き込む
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

				// writeLog 呼び出し失敗時はエラー内容を出力
				if (!response.ok) {
					const errText = await response.text();
					throw new Error("writeLog failed: " + response.status + " " + errText);
				}

				// getUserInfo Action / Function を呼び出す
				const userInfo = await fetch(`${sServiceUrl}getUserInfo`, {
					method: "POST",
					headers: {
						"Content-Type": "application/json",
						"X-CSRF-Token": csrfToken
					}
				});

				// getUserInfo 呼び出し失敗時はエラー内容を出力
				if (!userInfo.ok) {
					const errText = await userInfo.text();
					throw new Error("getUserInfo failed: " + userInfo.status + " " + errText);
				}

				// 取得結果をコンソールに出力
				console.log("userInfo:", await userInfo.json());
				console.log("success:", await response.json());

			} catch (e) {
				// 通信エラーや処理失敗時の共通エラーハンドリング
				console.error("request failed:", e);
			}
		}
	
	});
});