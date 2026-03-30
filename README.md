下面是未经验证过的版本，据说更好
sap.ui.define([
    "sap/ui/core/mvc/ControllerExtension"
], function (ControllerExtension) {
    "use strict";

    return ControllerExtension.extend("books.ext.controller.ListReportExtension", {
        override: {
            onInit: function () {
                setTimeout(() => {
                    this._callActions();
                }, 0);
            }
        },

        _callActions: async function () {
            const oModel = this.base.getView().getModel();

            console.log("oModel =", oModel);

            if (!oModel) {
                console.error("OData model not found");
                return;
            }

            try {
                const oWriteLog = oModel.bindContext("/writeLog(...)");
                oWriteLog.setParameter("objectType", "infomation");
                oWriteLog.setParameter("objectId", "000");
                oWriteLog.setParameter("operation", "init");
                oWriteLog.setParameter("detail", "画面初期化");
                await oWriteLog.execute();
                console.log("writeLog success:", oWriteLog.getBoundContext().getObject());

                const oUserInfo = oModel.bindContext("/getUserInfo(...)");
                await oUserInfo.execute();
                console.log("userInfo:", oUserInfo.getBoundContext().getObject());

            } catch (e) {
                console.error("request failed:", e);
            }
        }
    });
});
