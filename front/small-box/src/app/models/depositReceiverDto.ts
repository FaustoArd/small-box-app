import { DepositControlReceiverDto } from "./depositControlReceiverDto";

export class DepositReceiverDto{
   id!:number;

	receptionDate!:Date;

	organizationId!:number;

	 depositControlReceivers!:DepositControlReceiverDto[];
}