export class ReceiptDto {
    
    payment_type!: string;

    supplier_city!: Array<string>;

    supplier_address!: string;

    line_item!: Array<string>;

    total_price!: string;

    receipt_date!: string;

    receipt_code!: string;

    supplier_name!: Array<string>;
}