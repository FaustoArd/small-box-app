export class WorkTemplate{
    id!:number;
    date!:Date;
    corresponds!:string;
    correspondsNumber!:string;
    producedBy!:string;
    destinations!:Array<string>;
    refs!:Array<string>;
    text!:string;
    organizationId!:number;
}