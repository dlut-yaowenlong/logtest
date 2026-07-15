sap.ui.define([
	"sap/ui/core/mvc/ControllerExtension"
], async function (ControllerExtension) {
	"use strict";

	return ControllerExtension.extend("books.ext.controller.ListReportExtension", {
		override: {
			onInit: async function () {
				const oModel = this.base.getAppComponent().getModel();

				oModel.changeHttpHeaders({
					"X-Page-Type": "ListReport",
					"X-Screen-Name": "bookslistreport"
				});
				// 画面初期化ログ出力
				setTimeout(() => {
					this._logScreenInit();
				}, 0);

				// Export動作
				const controller = this.getView().getController();
				let oSmartTable = controller._getTable();
				if (!oSmartTable) {
					for (let i = 0; i < 100; i++) {
						await new Promise(r => setTimeout(r, 100));
						oSmartTable = controller._getTable();
						if (oSmartTable) {
							break;
						}
					}
				}

				if (!oSmartTable) {
					console.error("SmartTable not found");
					return;
				}

				if (typeof oSmartTable.attachBeforeExport === "function") {
					oSmartTable.attachBeforeExport(this._onBeforeExport, this);
					console.log("beforeExport attached");
				} else {
					console.warn("attachBeforeExport is not available on this table instance");
				}

				//　例外エラーハンドラ定義
				const oAppComponent = this.base.getAppComponent();
				// const oModel = oAppComponent && oAppComponent.getModel();
				console.log("final oModel =", oModel);

				// UI5输入エラー
				sap.ui.getCore().attachParseError(this._onParseError, this);
				sap.ui.getCore().attachValidationError(this._onValidationError, this);

				// グローバルエラー
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
		_onBeforeExport: async function (oEvent) {
			console.log("beforeExport", oEvent);

			const oView = this.base.getView();
			const oModel = oView && oView.getModel();

			if (!oModel) {
				console.error("OData model not found");
				return;
			}

			try {
				const sScreenName = "log test page";

				// beforeExportイベントからExcel出力情報を取得
				const oExportSettings = oEvent.getParameter("exportSettings") || {};

				const sDetail = JSON.stringify({
					logLevel: "info",
					screenName: sScreenName,
					message: "Excel export executed",
					fileName: oExportSettings.fileName || "",
					dataSource: oExportSettings.dataSource || {},
					columnCount: Array.isArray(oExportSettings.workbook?.columns)
						? oExportSettings.workbook.columns.length
						: 0,
					pageUrl: window.location.href
				});

				const vLogResult = await this._beforeExport(oModel, {
					pageType: "screen",
					pageName: sScreenName,
					detail: sDetail
				});

				console.log("beforeExport log result:", vLogResult);
			} catch (e) {
				console.error("beforeExport log request failed:", e);
			}
		},

		_beforeExport: async function (oModel, mParams) {
			const oActionBinding = oModel.bindContext("/beforeExport(...)");

			oActionBinding.setParameter("pageType", mParams.pageType);
			oActionBinding.setParameter("pageName", mParams.pageName);
			oActionBinding.setParameter("detail", mParams.detail);

			await oActionBinding.execute();

			const oContext = oActionBinding.getBoundContext();
			return oContext ? oContext.getObject() : null;
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