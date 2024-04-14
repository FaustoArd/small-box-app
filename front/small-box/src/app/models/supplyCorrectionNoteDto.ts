import { SupplyReportDto } from "./suppplyReportDto";

export class SupplyCorrectionNote{
    supplyNumber!:number;
    from!:string;
    to!:string;
    supplyReport!:Array<SupplyReportDto>;
    depositName!:string;

}