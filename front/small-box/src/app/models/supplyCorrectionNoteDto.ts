import { SupplyReportDto } from "./suppplyReportDto";

export class SupplyCorrectionNote{
    from!:string;
    to!:string;
    supplyReport!:Array<SupplyReportDto>;
}