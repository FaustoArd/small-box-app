export class DepositControlDto {
    id!: number
    place!: string;

    itemDescription!: string;

    itemCode!: string;
    quantity!: number;
    expirationDate!:Date;
    provider!: string;

    measureUnit!: string;
    itemUnitPrice!:number;
    itemTotalPrice!:number;
}