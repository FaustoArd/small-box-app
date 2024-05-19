import { SupplyReportDto } from "./suppplyReportDto";

export class SupplyCorrectionNote{
    supplyDate!:Date;
    supplyExcerciseYear!:number;
    supplyNumber!:number;
    from!:string;
    to!:string;
    supplyReport!:Array<SupplyReportDto>;
    depositName!:string;

}