import { PurchaseOrderItemDto } from "./PurchaseOrderItemDto";

export class PurchaseOrder{
    id!:number;
    orderNumber!:number;
    jurisdiction!:string;
    executerUnit!:string;
    executerUnitOrganizationId!:number;
    financingSource!:string;
    dependency!:string;
    dependencyOrganizacionId!:number;
    provider!:string;
    deliverTo!:string;
    date!:Date;
    exp!:string;
    purchaseOrderTotal!:number;
    items!:Array<PurchaseOrderItemDto>;
}