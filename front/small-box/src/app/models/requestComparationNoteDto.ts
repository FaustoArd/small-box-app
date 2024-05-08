import { ControlRequestComparationNoteDto } from "./controlRequestComparationNoteDto";

export class RequestComparationNoteDto{
    requestDate!:Date;
    fromOrganization!:string;
    mainOrganization!:string;
    requestCode!:string;
    depositName!:string;
    items!:ControlRequestComparationNoteDto[];
}