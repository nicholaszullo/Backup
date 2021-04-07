#python sentence_generation.py --task_type training --hidden_size 512 --embedding_dim 256 --batch_size 1024 --epochs 2
python sentence_generation.py --task_type generation --hidden_size 512 --embedding_dim 256 --generation_length 1000 --start_string JULIET --model_checkpoint experiments/exp_emb_256.h_512/best_model_colab.pt
