sap.ui.define(['sap/ui/core/mvc/ControllerExtension'], function (ControllerExtension) {
	'use strict';

	return ControllerExtension.extend('books.ext.controller.ObjectExtension', {
		// this section allows to extend lifecycle hooks or hooks provided by Fiori elements
		override: {
			/**
			 * Called when a controller is instantiated and its View controls (if available) are already created.
			 * Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
			 * @memberOf books.ext.controller.ObjectExtension
			 */
			onInit: function () {
				// you can access the Fiori elements extensionAPI via this.base.getExtensionAPI
				const oRouter = this.base.getAppComponent().getRouter();

				this._onRouteMatchedBound = this._onRouteMatched.bind(this);
				oRouter.attachRouteMatched(this._onRouteMatchedBound);
			}
		},
		_onRouteMatched: function (oEvent) {
			const sRouteName = oEvent.getParameter("name");
			const oModel = this.base.getAppComponent().getModel();

			if (!oModel || typeof oModel.changeHttpHeaders !== "function") {
				return;
			}

			if (sRouteName && sRouteName.toLowerCase().includes("object")) {
				oModel.changeHttpHeaders({
					"X-Page-Type": "ObjectPage",
					"X-Screen-Name": "booksobjectpage"
				});
			}
		}
	});
});
