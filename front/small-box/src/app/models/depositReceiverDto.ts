import { DepositControlReceiverDto } from "./depositControlReceiverDto";

export class DepositReceiverDto{
   id!:number;

	receptionDate!:Date;

	organizationId!:number;

	fromOrganizationName!:string;

	 depositControlReceivers!:DepositControlReceiverDto[];

	depositRequestCode!:string;

	readed!:boolean;
}