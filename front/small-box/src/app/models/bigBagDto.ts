import { bigBagItemDto } from "./bigBagItemDto";

export class BigBagDto{
    id!:number;
    name!:string;
    totalBigBagQuantityAvailable!:number;
    items!:bigBagItemDto[];

}