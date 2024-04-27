export class ExcelItemDto{
    excelItemId!:number;
    purchaseOrderId!:number;
   itemDescription!:string;
    itemMeasureUnit!:string;
    itemQuantity!:number;
    itemCode!:string;


    constructor(excelItemId:number,purchaseOrderId:number,itemCode:string,itemQuantity:number,itemMeasureUnit:string,itemDescription:string){
       this.excelItemId = excelItemId;
        this.purchaseOrderId = purchaseOrderId;
        this.itemCode= itemCode;
        this.itemQuantity = itemQuantity;
        this.itemMeasureUnit = itemMeasureUnit;
        this.itemDescription = itemDescription;
      
    }
}