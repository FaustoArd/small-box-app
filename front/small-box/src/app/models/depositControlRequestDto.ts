export class DepositControlRequestDto{
   	id!:number

	itemCode!:string;
	
	itemMeasureUnit!:string;
	
	 itemDescription!:string;
	
     itemQuantity!:number;
	
	 depositRequestId!:number;

	 constructor(itemCode:string,itemMeasureUnit:string,itemDescription:string,itemQuantity:number){
		this.itemCode = itemCode;
		this.itemMeasureUnit = itemMeasureUnit;
		this.itemDescription = itemDescription;
		this.itemQuantity = itemQuantity;
		
	 }
}