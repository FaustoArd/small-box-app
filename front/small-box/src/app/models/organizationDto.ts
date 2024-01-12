import { NumberSymbol } from "@angular/common";

export class OrganizationDto{
    id!:number;
    organizationName!:string;
    organizationNumber!:number;
    currentRotation!:number;
    maxRotation!:number;
    maxAmount!:number;
    responsible!:string;
    responsibleId!:number

}