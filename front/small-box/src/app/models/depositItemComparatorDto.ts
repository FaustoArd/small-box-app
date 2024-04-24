import { ExcelItemDto } from "./excelItemDto";
import { PurchaseOrderItemCandidateDto } from "./purchaseOrderItemCandidateDto";

export class DepositItemComparatorDto{
    excelItemDto!:ExcelItemDto;
    purchaseOrderItemCandidateDtos:PurchaseOrderItemCandidateDto[]=[];
}