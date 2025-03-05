#include <stdint.h>
#include <stdlib.h>
#include <stdio.h>

#define ROW_NUM 5
#define MAX_LEN 100

// filler data
int row_num = ROW_NUM;
int size_arr[ROW_NUM] = {3, 2, 5, 1, 4};
int matrix[ROW_NUM][MAX_LEN] = {{1024, 0, -32}, {5, 16}, {100, -234, 1, 21312, -13243}, {-342}, {-657, -62, -891, -5}};

// Three-element input data
struct input_t {
   uint16_t row_num;
   uint16_t **matrix;
   uint16_t *size;
};

struct input_t* init_input() {
   struct input_t *in = (struct input_t*) malloc(sizeof(struct input_t));
   in->row_num = row_num;
   in->size = (uint16_t*) malloc(sizeof(uint16_t) * in->row_num);
   in->matrix = (uint16_t**) malloc(sizeof(uint16_t*) * in->row_num);
   for (int i = 0; i < in->row_num; i++) {
      in->size[i] = size_arr[i];
   }
   for (int i = 0; i < in->row_num; i++) {
      in->matrix[i] = (uint16_t*) malloc(sizeof(uint16_t) * in->size[i]);
      for (int j = 0; j < in->size[i]; j++) {
         in->matrix[i][j] = matrix[i][j];
      }
   }
   return in;
}

uint16_t f(uint16_t x) {
   return -x + 1;
}

int main() {
   struct input_t* in = init_input();
   printf("##### 2D array before f(x):\n");
   for (int i = 0; i < in->row_num; i++) {
      for (int j = 0; j < in->size[i]; j++) {
         printf("%d\t", in->matrix[i][j]);
      }
      printf("\n");
   }
   printf("\n##### 2D array after f(x):\n");
   for (int i = 0; i < in->row_num; i++) {
      for (int j = 0; j < in->size[i]; j++) {
         uint16_t* ptr = &in->matrix[i][j];
         *ptr = f(*ptr);
         printf("%d\t", *ptr);
      }
      printf("\n");
   }
   return 0;
}