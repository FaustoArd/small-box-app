import { NumberSymbol } from "@angular/common";
import { ControlRequestComparationNoteDto } from "./controlRequestComparationNoteDto";

export class RequestComparationNoteDto{
    receiverId!:number;
    requestDate!:Date;
    fromOrganization!:string;
    mainOrganization!:string;
    requestCode!:string;
    depositName!:string;
    items!:ControlRequestComparationNoteDto[];
}