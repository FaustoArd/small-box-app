export class PurchaseOrderItemDto{
    code!:string;
    programaticCat!:string;
    quantity!:number;
    measureUnit!:string;
    itemDetail!:string;
    unitCost!:number;
    estimatedCost!:number;
    totalEstimatedCost!:number;
    expirationDate!:Date;
}