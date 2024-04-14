import { PurchaseOrderItemDto } from "./purchaseOrderItemDto";


export class PurchaseOrderDto {
    id!: number;
    orderNumber!: number;
    jurisdiction!: string;
    executerUnit!: string;
    executerUnitOrganizationId!: number;
    financingSource!: string;
    dependency!: string;
    dependencyOrganizacionId!: number;
    provider!: string;
    deliverTo!: string;
    date!: Date;
    exp!: string;
    purchaseOrderTotal!: number;
    items: Array<PurchaseOrderItemDto> = [];
    loadedToDeposit!: boolean;
    loadedToDepositId!: number;
    loadedToDepositName!: string;
}