sap.ui.define([
	"sap/ui/core/mvc/ControllerExtension"
], function (ControllerExtension) {
	"use strict";

	return ControllerExtension.extend("books.ext.controller.ListReportExtension", {
		override: {
			onInit: function () {
				// 画面初期化ログ出力
				setTimeout(() => {
					this._logScreenInit();
				}, 0);

				//　例外エラーハンドラ定義
				const oAppComponent = this.base.getAppComponent();
				const oModel = oAppComponent && oAppComponent.getModel();
 console.log("final oModel =", oModel);
				// if (oModel) {
				// 	// OData请求失败
				// 	if (oModel.attachRequestFailed) {
				// 		oModel.attachRequestFailed(this._onRequestFailed, this);
				// 	}

				// 	// metadata加载失败
				// 	if (oModel.attachMetadataFailed) {
				// 		oModel.attachMetadataFailed(this._onMetadataFailed, this);
				// 	}
				// }

				// UI5输入解析/校验错误
				sap.ui.getCore().attachParseError(this._onParseError, this);
				sap.ui.getCore().attachValidationError(this._onValidationError, this);

				// JS全局未捕获异常
				window.addEventListener("error", this._onWindowErrorBound = this._onWindowError.bind(this));
				window.addEventListener("unhandledrejection", this._onUnhandledRejectionBound = this._onUnhandledRejection.bind(this));
			},
			onExit: function () {
				const oView = this.base.getView();
				const oModel = oView && oView.getModel();

				if (oModel) {
					if (oModel.detachRequestFailed) {
						oModel.detachRequestFailed(this._onRequestFailed, this);
					}
					if (oModel.detachMetadataFailed) {
						oModel.detachMetadataFailed(this._onMetadataFailed, this);
					}
				}

				sap.ui.getCore().detachParseError(this._onParseError, this);
				sap.ui.getCore().detachValidationError(this._onValidationError, this);

				if (this._onWindowErrorBound) {
					window.removeEventListener("error", this._onWindowErrorBound);
				}
				if (this._onUnhandledRejectionBound) {
					window.removeEventListener("unhandledrejection", this._onUnhandledRejectionBound);
				}
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

		_onInit: async function (oModel, mParams) {
			const oActionBinding = oModel.bindContext("/onInit(...)");

			oActionBinding.setParameter("pageType", mParams.pageType);
			oActionBinding.setParameter("pageName", mParams.pageName);
			oActionBinding.setParameter("detail", mParams.detail);

			await oActionBinding.execute();

			const oContext = oActionBinding.getBoundContext();
			return oContext ? oContext.getObject() : null;
		}
		,


		_onRequestFailed: function (oEvent) {
			console.log("_onRequestFailed")
			const p = oEvent.getParameters ? oEvent.getParameters() : {};
			this._sendLogToServer({
				category: "ODATA_REQUEST_FAILED",
				message: p.message || "OData request failed",
				statusCode: p.statusCode || "",
				statusText: p.statusText || "",
				responseText: p.responseText || "",
				url: p.url || "",
				method: p.method || "",
				page: window.location.href
			});
		},

		_onMetadataFailed: function (oEvent) {
			console.log("_onMetadataFailed")
			const p = oEvent.getParameters ? oEvent.getParameters() : {};
			this._sendLogToServer({
				category: "ODATA_METADATA_FAILED",
				message: p.message || "Metadata load failed",
				statusCode: p.statusCode || "",
				statusText: p.statusText || "",
				responseText: p.responseText || "",
				page: window.location.href
			});
		},

		_onParseError: function (oEvent) {
			console.log("_onParseError")
			const p = oEvent.getParameters ? oEvent.getParameters() : {};
			this._sendLogToServer({
				category: "UI_PARSE_ERROR",
				message: p.newValue || "Parse error",
				page: window.location.href
			});
		},

		_onValidationError: function (oEvent) {
			console.log("_onValidationError")
			const p = oEvent.getParameters ? oEvent.getParameters() : {};
			this._sendLogToServer({
				category: "UI_VALIDATION_ERROR",
				message: p.newValue || "Validation error",
				page: window.location.href
			});
		},

		_onWindowError: function (oEvent) {
			console.log("_onWindowError")
			this._sendLogToServer({
				category: "JS_RUNTIME_ERROR",
				message: oEvent.message || "Unhandled JS error",
				fileName: oEvent.filename || "",
				line: oEvent.lineno || "",
				column: oEvent.colno || "",
				stack: oEvent.error && oEvent.error.stack ? oEvent.error.stack : "",
				page: window.location.href
			});
		},

		_onUnhandledRejection: function (oEvent) {
			console.log("_onUnhandledRejection")
			const reason = oEvent.reason;
			this._sendLogToServer({
				category: "PROMISE_REJECTION",
				message: reason && reason.message ? reason.message : String(reason),
				stack: reason && reason.stack ? reason.stack : "",
				page: window.location.href
			});
		},

		_sendLogToServer: async function (oLog) {
			console.log("sendLogToServer start")
			console.log(oLog)
		}
	});
});