import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { ContainerService } from 'src/app/services/container.service';
import { ContainerDto } from 'src/app/models/containerDto';
import { Router } from '@angular/router';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-container',
  templateUrl: './container.component.html',
  styleUrls: ['./container.component.css']
})
export class ContainerComponent implements OnInit {


container!:ContainerDto;
errorData!:string;
returnedData!:ContainerDto;
smallBoxTypes: string[] = [];

deps :string[] =  ['Secretaria de Desarrollo Social', 'Direccion de Administracion y Despacho'
,'Subsecretaria de Politicas Socio Comunitarias']

  constructor(private formBuilder: FormBuilder,private containerService:ContainerService,
    private router:Router,private storageService:StorageService){ }


  ngOnInit(): void {
    this.getSmallBoxTypes();
  }

  getSmallBoxTypes(){
    this.smallBoxTypes = ["CHICA", "ESPECIAL"];
  }

  containerFormBuilder = this.formBuilder.group({
   title:['', Validators.required],
    dependency:['', Validators.required],
    responsible:['',Validators.required]
});

get title(){
    return this.containerFormBuilder.controls.title;
  }
  get dependency(){
    return this.containerFormBuilder.controls.dependency;
  }

  get responsible(){
    return this.containerFormBuilder.controls.responsible;
  }

  onCreateContainer(){
    if(this.containerFormBuilder.valid){
      this.container = new ContainerDto();
      this.container = Object.assign(this.container,this.containerFormBuilder.value)
      this.containerService.createContainer(this.container).subscribe({
        next:(contData)=>{
          this.returnedData = contData;
          this.storageService.deleteCurrentContainerId();
          this.storageService.setCurrentContainerId(JSON.stringify(this.returnedData.id));
          console.log(this.storageService.getCurrentContainerId());

        },
        error:(errorData)=>{
          this.errorData = errorData;
        },
        complete:()=>{
          this.router.navigateByUrl("/small-box")
        }
      })
    }
  }

  
 


}
