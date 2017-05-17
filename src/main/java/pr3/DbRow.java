package pr3;

import java.time.LocalDate;

/**
 * Строка таблицы plna_items
 * Created by Дмитрий on 17.05.2017.
 */
public class DbRow {

    private Integer plna_id; // bigint(20) UN AI PK
    private String plna_source_type; //  varchar(100)
    private Integer plna_source_id; //  int(11)
    private Integer plna_row_num; //  int(11)
    private String plna_parent_rows_list; // varchar(255)
    private String plna_diag_statcode; //  varchar(255)
    private LocalDate plna_date_from; //  date
    private LocalDate plna_date_to; //  date
    private String plna_item_text; //  text
    private String plna_item_text_extra; //  text
    private String plna_item_descr; // text
    private String plna_item_cat0; //  text
    private String plna_item_cat1; //  text
    private String plna_item_cat2; //  text
    private String plna_item_cat3; //  text
    private String plna_item_cat4; //  text
    private String plna_item_currency; //  varchar(10)
    private Double plna_currency_rate; //  decimal(10,4)
    private Double plna_vat_rate; // decimal(10,5)
    private String plna_units; //  varchar(50)
    private Double plna_price_1; //  decimal(15,2)
    private Double plna_price_rub_1; //  decimal(15,2)
    private Double plna_price_many; // decimal(15,2)
    private Double plna_price_rub_many; // decimal(15,2)
    private Double plna_price_dealer; // decimal(15,2)
    private Double plna_price_rub_dealer; // decimal(15,2)
    private Double plna_price_dealer2; //  decimal(15,2)
    private Double plna_price_rub_dealer2; //  decimal(15,2)
    private String plna_in_stock_state; // varchar(50)
    private Double plna_discount_proc; //  decimal(10,2)
    private String plna_brand; //  varchar(50)
    private String plna_seller; // varchar(50)
    private String plna_article_code; // varchar(50)
    private String plna_article_type; // varchar(50)
    private String plna_article_code1; //  varchar(50)
    private String plna_item_code; //  varchar(50)
    private String plna_order_code; // varchar(50)
    private String plna_minimal_order; //  varchar(50)
    private Double plna_minimal_order_sum; //  decimal(15,2)
    private String plna_part_num; // varchar(50)
    private String plna_extra_data; // text
    private Integer plna_sys_row_updated; //  int(11)
}
