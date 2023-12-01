export class ObjectAssigner{

    static create<T>(init: Partial<T>): T{
        const value = {};
        Object.assign(value,init);
        return value as T;

    }
}