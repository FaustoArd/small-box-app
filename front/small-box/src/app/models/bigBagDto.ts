import { BigBagItemDto } from "./bigBagItemDto";

export class BigBagDto{
    id!:number;
    name!:string;
    creationDate!:Date;
    totalBigBagQuantityAvailable!:number;
    items!:BigBagItemDto[];
   
}