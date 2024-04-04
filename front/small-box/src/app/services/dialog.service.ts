import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DialogTemplateData } from '../models/dialogTemplateData';
import { DialogTemplateComponent } from '../components/dialog/dialog-template/dialog-template.component';

@Injectable({
  providedIn: 'root'
})
export class DialogService {

  constructor(private matDialog:MatDialog) { }

  openDialogCreation(data:DialogTemplateData){
    return this.matDialog.open(DialogTemplateComponent,{ data });
  }

  openDialogItemCreation(data:DialogTemplateData){
    return this.matDialog.open(DialogTemplateComponent,{
      width:'40%',
      height:'70%',
      data
    })
  }

  openSupplyCorrectionNoteCreation(data:DialogTemplateData){
    return this.matDialog.open(DialogTemplateComponent,{
      width:'100%',
      height:'100%',
      data
    })
  }
}
