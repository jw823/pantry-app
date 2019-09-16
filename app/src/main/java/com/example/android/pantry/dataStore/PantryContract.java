package com.example.android.pantry.dataStore;

import android.provider.BaseColumns;

/**
 * Created by dewong4 on 5/12/17.
 */

public class PantryContract {

    public static final class ProductsEntry implements BaseColumns {
        public static final String TABLE_NAME = "Products";

        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_INGREDIENT = "ingredient";
        public static final String COLUMN_BRAND = "brand";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_UNIT = "unit";
        public static final String COLUMN_DESCRIPTION = "desc";
        public static final String COLUMN_DESCRIPTION2 = "desc2";
    }

    static final class CategoriesEntry implements BaseColumns {
        static final String TABLE_NAME = "Categories";

        static final String COLUMN_CATEGORY_ID = "category_id";
        static final String COLUMN_NAME = "name";
    }

    static final class IngredientsEntry implements BaseColumns {
        static final String TABLE_NAME = "Ingredients";

        static final String COLUMN_INGREDIENT_ID = "ingredient_id";
        static final String COLUMN_TYPE = "type";
        static final String COLUMN_NAME = "name";
    }

    static final class BarcodesEntry implements BaseColumns {
        static final String TABLE_NAME = "Barcodes";

        static final String COLUMN_BARCODE_ID = "barcode_id";
        static final String COLUMN_PRODUCT_ID = "product_id";
        static final String COLUMN_TYPE = "type";
        static final String COLUMN_VALUE = "value";
    }

    static final class PLUsEntry implements BaseColumns {
        static final String TABLE_NAME = "PLUs";

        static final String COLUMN_PLU_ID = "plu_id";
        static final String COLUMN_PRODUCT_ID = "product_id";
        static final String COLUMN_TYPE = "type";
        static final String COLUMN_VALUE = "value";
    }

    static final class ImagesEntry implements BaseColumns {
        static final String TABLE_NAME = "Imagess";

        static final String COLUMN_IMAGE_ID = "image_id";
        static final String COLUMN_PRODUCT_ID = "product_id";
        static final String COLUMN_TYPE = "type";
        static final String COLUMN_DATA = "dataStore";
    }

    public static final class InventoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "InventoryTable";

        public static final String COLUMN_INVENTORY_ID = "inventory_id";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_EXPIRATION_DATE = "expiration_date";
        public static final String COLUMN_PURCHASE_DATE = "purchase_date";
        public static final String COLUMN_PURCHASE_PRICE = "purchase_price";
    }

    static final class LocationsEntry implements BaseColumns {
        static final String TABLE_NAME = "Locations";

        static final String COLUMN_LOCATION_ID = "location_id";
        static final String COLUMN_DESCRIPTION = "desc";

    }
}
