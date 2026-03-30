using CatalogService as service from '../../srv/catalog-service';
annotate service.Books with @(
    UI.FieldGroup #GeneratedGroup : {
        $Type : 'UI.FieldGroupType',
        Data : [
            {
                $Type : 'UI.DataField',
                Label : 'title',
                Value : title,
            },
            {
                $Type : 'UI.DataField',
                Label : 'author',
                Value : author,
            },
            {
                $Type : 'UI.DataField',
                Label : 'stock',
                Value : stock,
            },
            {
                $Type : 'UI.DataField',
                Label : 'price',
                Value : price,
            },
        ],
    },
    UI.Facets : [
        {
            $Type : 'UI.ReferenceFacet',
            ID : 'GeneratedFacet1',
            Label : 'General Information',
            Target : '@UI.FieldGroup#GeneratedGroup',
        },
    ],
    UI.LineItem : [
        {
            $Type : 'UI.DataField',
            Label : 'title',
            Value : title,
        },
        {
            $Type : 'UI.DataField',
            Label : 'author',
            Value : author,
        },
        {
            $Type : 'UI.DataField',
            Label : 'stock',
            Value : stock,
        },
        {
            $Type : 'UI.DataField',
            Label : 'price',
            Value : price,
        },
        {
            $Type : 'UI.DataField',
            Value : categoryCode,
        },
        {
            $Type : 'UI.DataField',
            Value : ID,
        },
    ],
    UI.SelectionFields : [
        author,
        categoryCode,
        ID,
        price,
        stock,
        title,
    ],
);

annotate service.Books with {
    author @Common.Label : 'author'
};

annotate service.Books with {
    categoryCode @(
        Common.Label : 'categoryCode',
        Common.ValueList : {
            $Type : 'Common.ValueListType',
            CollectionPath : 'Categories',
            Parameters : [
                {
                    $Type : 'Common.ValueListParameterInOut',
                    LocalDataProperty : categoryCode,
                    ValueListProperty : 'code',
                },
            ],
        },
        Common.ValueListWithFixedValues : true,
    )
};

annotate service.Books with {
    ID @Common.Label : 'ID'
};

annotate service.Books with {
    price @Common.Label : 'price'
};

annotate service.Books with {
    stock @Common.Label : 'stock'
};

annotate service.Books with {
    title @Common.Label : 'title'
};

