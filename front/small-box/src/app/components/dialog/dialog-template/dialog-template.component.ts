import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DialogTemplateData } from 'src/app/models/dialogTemplateData';

@Component({
  selector: 'app-dialog-template',
  templateUrl: './dialog-template.component.html',
  styleUrls: ['./dialog-template.component.css']
})
export class DialogTemplateComponent {

  constructor( @Inject(MAT_DIALOG_DATA) public data:DialogTemplateData){}

}
