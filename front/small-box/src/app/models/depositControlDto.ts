export class DepositControlDto {
    id!: number

    supplyNumber!: string;
    place!: string;

    itemName!: string;

    itemCode!: string;
    quantity!: number;
    expirationDate!:Date;
    provider!: string;

    measureUnit!: number;
    itemUnitPrice!:number;
    itemTotalPrice!:number;
}