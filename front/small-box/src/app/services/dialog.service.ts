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
}
