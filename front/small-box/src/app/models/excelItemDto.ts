export class ExcelItemDto{
    excelItemId!:number;
    purchaseOrderItemId!:number;
   itemDescription!:string;
    itemMeasureUnit!:string;
    itemQuantity!:number;
    itemCode!:string;


    constructor(excelItemId:number,purchaseOrderItemId:number,itemCode:string,itemQuantity:number,itemMeasureUnit:string,itemDescription:string){
       this.excelItemId = excelItemId;
        this.purchaseOrderItemId = purchaseOrderItemId;
        this.itemCode= itemCode;
        this.itemQuantity = itemQuantity;
        this.itemMeasureUnit = itemMeasureUnit;
        this.itemDescription = itemDescription;
      
    }
}