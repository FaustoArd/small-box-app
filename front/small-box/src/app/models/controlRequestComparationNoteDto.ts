export class ControlRequestComparationNoteDto{
    requestItemCode!:string;
    requestItemMeasureUnit!:string;
    requestItemDescription!:string;
    requestItemQuantity!:number;
    controlItemCode!:string;
    controlItemMeasureUnit!:string;
    controlItemDescription!:string;
    controlItemQuantity!:number;
    controlQuantityLeft!:number;
}