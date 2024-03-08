import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.css']
})
export class ConfirmDialogComponent implements OnInit {

  title!:string;
  message!:string;
  btnOkText!:string;
  btnCancelText!:string;

constructor(public dialogRef:MatDialogRef<ConfirmDialogComponent>,
  @Inject(MAT_DIALOG_DATA)public data:ConfirmDialogComponent){
    this.title = data.title;
    this.message = data.message;
    this.btnOkText = data.btnOkText;
    this.btnCancelText = data.btnCancelText;
  }

  ngOnInit(): void {
      
  }

  confirm(){
    this.dialogRef.close(true);

  }
  cancel(){
    this.dialogRef.close(false);
  }
}
