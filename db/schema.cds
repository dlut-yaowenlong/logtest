namespace my;

entity Books {
    key ID       : UUID;
    title        : String(100);
    author       : String(100);
    category : Association to Categories;
    stock        : Integer;
    price        : Decimal(9,2);
}

entity Categories {
    key code : String(10);
    name     : String(100);
}