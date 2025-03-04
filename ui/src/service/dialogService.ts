import Swal from "sweetalert2";

export function showError(errorMessage: string){
    Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: errorMessage,
        color: "red",   
        confirmButtonColor: "#007bff"
      });
}

export function withConfimration(): Promise<boolean> {

    return new Promise((resolve) => {
        Swal.fire({
            icon: 'question',
            title: 'Are you sure?',
            showCancelButton: true,
            confirmButtonColor: "#007bff",
        }).then((result) => {
            if (result.isConfirmed) {
                resolve(true);
            }
            else {
                resolve(false);
            }
        });
    });
}