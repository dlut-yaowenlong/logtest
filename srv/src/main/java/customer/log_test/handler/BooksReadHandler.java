package customer.log_test.handler;

import com.sap.cds.Result;
import com.sap.cds.services.cds.CdsReadEventContext;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.ServiceName;
import cds.gen.catalogservice.Books_;
import cds.gen.catalogservice.CatalogService_;
import com.sap.cds.services.request.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import customer.log_test.util.LogUtil;
import java.util.List;
import java.util.Map;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class BooksReadHandler implements EventHandler {

    @Autowired
    private UserInfo userInfo;

    @After(event = "READ", entity = Books_.CDS_NAME)
    public void onAfterReadBooks(CdsReadEventContext context, Result result) {
        List<Map> books = result.listOf(Map.class);
        
        StringBuilder sb = new StringBuilder();
        sb.append("Books READ result count=").append(books.size());

        if (!books.isEmpty()) {
            sb.append(" | data=");
            for (Map book : books) {
                sb.append(book).append(" ; ");
            }
        }

        LogUtil.log(
                userInfo.getName(),
                "log test page",
                "INFO",
                sb.toString());
    }
}