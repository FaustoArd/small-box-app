export class DepositDependencyControlDto {
    id!: number;

    place!: string;

    itemDescription!: string;

    itemCode!: string;

    quantity!: number;

    expirationDate!: Date;

    provider!: string;

    measureUnit!: string;

    itemUnitPrice!: number;

    itemTotalPrice!: number;

    mainOrganization!: string;

    applicantOrganization!: string;

    applicantOrganizationId!: number;

    depositName!: string;

    depositId!: number;

}