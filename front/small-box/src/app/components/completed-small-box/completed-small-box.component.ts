import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';
import { ContainerDto } from 'src/app/models/containerDto';
import { SmallBoxUnifierDto } from 'src/app/models/smallBoxUnifierDto';
import { ContainerService } from 'src/app/services/container.service';
import { SmallBoxService } from 'src/app/services/small-box.service';
import { StorageService } from 'src/app/services/storage.service';
import autoTable from 'jspdf-autotable'
import { auto } from '@popperjs/core';
import { toPng, toJpeg, toBlob, toPixelData, toSvg } from 'html-to-image';
import * as htmlToImage from 'html-to-image';
import { CookieStorageService } from 'src/app/services/cookie-storage.service';




@Component({
  selector: 'app-completed-small-box',
  templateUrl: './completed-small-box.component.html',
  styleUrls: ['./completed-small-box.component.css']
})
export class CompletedSmallBoxComponent implements OnInit {

  completedSmallBox: SmallBoxUnifierDto[] = [];
  errorData!: string;
  container!: ContainerDto;
  smallBoxCreated!: boolean;




  constructor(private smallBoxService: SmallBoxService, private containerService: ContainerService,
    private cookieService: CookieStorageService, private route: ActivatedRoute, private router: Router) { }


  ngOnInit(): void {
    const containerId = Number(this.route.snapshot.paramMap.get('id'));
    this.containerService.getContainerById(containerId).subscribe({
      next: (containerData) => {
        this.container = new ContainerDto();
        this.container = containerData;
        this.cookieService.setCurrentContainerId(JSON.stringify(containerData.id));
        this.smallBoxCreated = containerData.smallBoxCreated;
        if (this.smallBoxCreated) {
          this.deleteAllbyContainerId(Number(this.cookieService.getCurrentContainerId()));
          this.onCompleteSmallBox();

        } else {
          this.deleteAllbyContainerId(Number(this.cookieService.getCurrentContainerId()));
          this.onCompleteSmallBox();

        }
      }, error: (errorData) => {
        this.errorData = errorData;
      }
    });





  }


  onCompleteSmallBox(): void {
    this.smallBoxService.completeSmallBox(Number(this.cookieService.getCurrentContainerId())).subscribe({
      next: (compData) => {
        this.completedSmallBox = compData;
      },
      error: (errorData) => {
        this.errorData = errorData;
      }, complete: () => {
        this.getContainerById();
      }
    });
  }
  getContainerById(): void {
    this.containerService.getContainerById(Number(this.cookieService.getCurrentContainerId())).subscribe({
      next: (containerData) => {
        this.container = containerData;
        this.smallBoxCreated = containerData.smallBoxCreated;

      }, error: (errorData) => {
        this.errorData = errorData;
      }
    });
  }

  getCompletedSmallboxByContainerId(id: number): void {
    this.smallBoxService.getCompletedSmallBoxByContainerId(id).subscribe({
      next: (completedData) => {
        this.completedSmallBox = completedData;
      },
      error: (erorrData) => {
        this.errorData = erorrData;
      },
      complete: () => {
        this.router.navigateByUrl("/completed");
      }
    });
  }

  deleteAllbyContainerId(containerId: number): void {
    this.smallBoxService.deleteAllByContainerId(containerId).subscribe();
  }

  exportToPdf(pages: HTMLElement) {
    const doc = new jsPDF({
      unit: 'px',

    })
  }
  captureScreen(): void {
    const filename = 'test.pdf';
    var node: any = document.getElementById('contentToConvert');
    htmlToImage.toPng(node)
      .then(function (dataUrl) {
        var img = new Image();
        img.src = dataUrl;
        const pdf = new jsPDF('p', 'mm', 'a4');
        pdf.setLineWidth(1);
        pdf.addImage(img, 'PNG', 0, 0, 208, 298);
        pdf.save(filename);

      })
      .catch(function (error) {
        console.error('something went wrong', error);
      })
  }



}