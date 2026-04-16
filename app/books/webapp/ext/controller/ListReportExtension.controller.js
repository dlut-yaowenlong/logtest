sap.ui.define([
	"sap/ui/core/mvc/ControllerExtension"
], function (ControllerExtension) {
	"use strict";

	return ControllerExtension.extend("books.ext.controller.ListReportExtension", {
		override: {
			onInit: function () {
				setTimeout(() => {
					this._logScreenInit();
				}, 0);
			}
		},

		// 画面初期化時のログ出力
		_logScreenInit: async function () {
			const oView = this.base.getView();
			const oModel = oView.getModel();

			if (!oModel) {
				console.error("OData model not found");
				return;
			}

			try {
				// 画面名は必要に応じて業務名称に変更
				const sScreenName = "log test page";

				// 2. ログ出力
				const sDetail = JSON.stringify({
					logLevel: "info",
					screenName: sScreenName,
					message: "page initialize"
				});

				const vLogResult = await this._onInit(oModel, {
					pageType: "screen",
					pageName: sScreenName,
					detail: sDetail
				});

				// console.log("writeLog result:", vLogResult);
			} catch (e) {
				console.error("request failed:", e);
			}
		},

		// Action: getUserInfo()
		// _getUserInfo: async function (oModel) {
		// 	const oActionBinding = oModel.bindContext("/getUserInfo(...)");

		// 	await oActionBinding.execute();

		// 	const oContext = oActionBinding.getBoundContext();
		// 	return oContext ? oContext.getObject() : null;
		// },

		// Action: writeLog(...)
		_onInit: async function (oModel, mParams) {
			const oActionBinding = oModel.bindContext("/onInit(...)");

			oActionBinding.setParameter("pageType", mParams.pageType);
			oActionBinding.setParameter("pageName", mParams.pageName);
			oActionBinding.setParameter("detail", mParams.detail);

			await oActionBinding.execute();

			const oContext = oActionBinding.getBoundContext();
			return oContext ? oContext.getObject() : null;
		}
	});
});