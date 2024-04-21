export class BigBagItemDto{
   id!:number;
	
	 code!:string;
	
	 measureUnit!:string;
	
	 quantity!:number;

	 description!:string;
	
	 unitCost!:number;
	
	 totalCost!:number;
	
	 expirationDate!:Date;

	 depositControlId!:number;

	 bigBagId!:number;

	 

	 constructor(code:string,measureUnit:string,description:string,depositControlId:number){
		this.code = code;
		this.measureUnit = measureUnit;
		this.description = description;
		this.depositControlId = depositControlId;
	 }
}