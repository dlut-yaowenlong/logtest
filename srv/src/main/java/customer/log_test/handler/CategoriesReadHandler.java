package customer.log_test.handler;

import com.sap.cds.services.cds.CdsReadEventContext;
import com.sap.cds.services.handler.EventHandler;

import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.ServiceName;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sap.cds.services.request.UserInfo;
import cds.gen.catalogservice.CatalogService_;
import customer.log_test.util.LogUtil;
import com.sap.cds.Result;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class CategoriesReadHandler implements EventHandler {
    @Autowired
    private UserInfo userInfo;

    @After(event = "READ", entity = "CatalogService.Categories")
    public void onReadCategories(CdsReadEventContext context, Result result) {
        List<Map> categories = result.listOf(Map.class);

        String message;
        if (categories == null) {
            message = "Categories read result is null";
        } else {
            StringBuilder sb = new StringBuilder();
            int count = 0;

            for (var row : categories) {
                count++;
                sb.append(row.toString()).append(" ; ");
            }

            message = "Categories read count=" + count + " , data=" + sb;
        }

        LogUtil.log(
                userInfo.getName(),
                "log test page",
                "INFO",
                message
        );
    }
}