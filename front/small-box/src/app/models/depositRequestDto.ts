import { DepositControlRequestDto } from "./depositControlRequestDto";

export class DepositRequestDto{
     id!:number;
	requestDate!:Date;
	organizationId!:number;
	depositControlRequestDtos!:DepositControlRequestDto[];
	requestCode!:string;

}