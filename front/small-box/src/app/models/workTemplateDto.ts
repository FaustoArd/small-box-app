export class WorkTemplateDto{
    id!:number;
    date!:Date;
    correspond!:string;
    correspondNumber!:string;
    producedBy!:string;
    destinations!:Array<string>;
    refs!:Array<string>;
    items!:Array<string>;
    text!:string;
    organizationId!:number;
}