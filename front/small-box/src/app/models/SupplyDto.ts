import { SupplyItemDto } from "./supplyItemDto";

export class SupplyDto{
    id!:number;
    date!:Date;
    jurisdiction!:string;
    supplyNumber!:number;
    supplyItems!:Array<SupplyItemDto>;
    estimatedTotalCost!:number;
    dependencyApplicant!:string;
    dependecyApplicantOrganizationId!:number;
    applicantOrganization!:string;
}