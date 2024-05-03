import { DepositControlRequestDto } from "./depositControlRequestDto";

export class DepositRequestDto{
     id!:number;
	requestDate!:Date;
	mainOrganizationId!:number;
	destinationOrganizationName!:string;
	destinationOrganizationId!:number;
	depositControlRequestDtos!:DepositControlRequestDto[];
	requestCode!:string;

}