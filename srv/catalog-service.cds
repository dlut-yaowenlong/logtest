using {my} from '../db/schema';

service CatalogService {
    entity Books      as projection on my.Books;
    entity Categories as projection on my.Categories;
    // action getUserInfo()            returns UserInfo;

    action onInit(pageType: String,
                    pageName: String,
                    detail: String) returns String;
}

// type UserInfo {
//     userId   : String;
//     userName : String;
//     email    : String;
// }
