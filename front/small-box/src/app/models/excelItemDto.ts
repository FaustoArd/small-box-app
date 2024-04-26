export class ExcelItemDto{
    excelItemId!:number;
    purchaseOrderId!:number;
    itemDescription!:string;
    itemMeasureUnit!:string;
    itemQuantity!:number;


    constructor(purchaseOrderId:number,itemQuantity:number){
        this.purchaseOrderId = purchaseOrderId;
        this.itemQuantity = itemQuantity;
    }
}