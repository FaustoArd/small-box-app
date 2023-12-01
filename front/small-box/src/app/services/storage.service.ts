import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  constructor() { }


  getCurrentContainerId(){
    return window.localStorage.getItem("containerId");
  }

  setCurrentContainerId(containerId:string){
    window.localStorage.setItem("containerId", containerId);
  }
  deleteCurrentContainerId(){
    window.localStorage.removeItem("containerId");
  }
}
